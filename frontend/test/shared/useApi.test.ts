import apiClient from "@/plugins/axios.ts";
import useApi from "@/shared/composables/useApi.ts";
import {useToastService} from "@/shared/composables/toastService.ts";

vi.mock('@/shared/composables/toastService', () => {
  return {
    useToastService: vi.fn().mockReturnValue({
      showError: vi.fn(),
      showSuccess: vi.fn(),
    }),
  };
});
vi.mock('@/plugins/axios.ts');

describe('useApi', () => {

  beforeEach(() => {
    vi.clearAllMocks();
  });

  const testUrl = '/testUrl';

  it('should show thrown error if Axios throws', async () => {
    const testErrorMessage = 'Test Error';
    vi.mocked(apiClient.delete).mockRejectedValue(new Error(testErrorMessage));

    try {
      await useApi().delete(testUrl);
    } catch (e) {
      expect(e).toBeInstanceOf(Error);
    }
    expect(apiClient.delete).toHaveBeenCalledWith(testUrl);
    expect(useToastService().showError).toHaveBeenCalledWith('Failed to delete', testErrorMessage);
  });

  it.each(
    [undefined, 'Congrats - you deleted it!']
  )('should show %s on delete with status 200', async (customSuccess) => {
    vi.mocked(apiClient.delete).mockResolvedValue({status: 200});
    const successMessage = customSuccess?? `Successfully deleted ${testUrl}!`;

    await useApi().delete(testUrl, {successMessage: successMessage});
    expect(apiClient.delete).toHaveBeenCalledWith(testUrl);
    expect(useToastService().showSuccess).toHaveBeenCalledWith(successMessage);
  });

  it.each([undefined, 'Error - cannot delete that!']
  )('should show %s on delete with Status 403', async (customError) => {
    vi.mocked(apiClient.delete).mockResolvedValue({status: 403});
    const errorMessage = customError?? 'Failed to delete';

    await useApi().delete(testUrl, {errorMessage: errorMessage});
    expect(apiClient.delete).toHaveBeenCalledWith(testUrl);
    expect(useToastService().showError).toHaveBeenCalledWith(errorMessage, 'Error status code 403!');
  });

  it.each([200, 201]
  )('should show post success on axios code %i', async (code) => {
    vi.mocked(apiClient.post).mockResolvedValue({status: code});
    const requestData = {someData: 'someData'};

    await useApi().post(testUrl, requestData);
    expect(apiClient.post).toHaveBeenCalledWith(testUrl, requestData);
    expect(useToastService().showSuccess).toHaveBeenCalledWith(`Successfully posted to ${testUrl}!`);
  });

  it('should show put success on axios code 200', async () => {
    vi.mocked(apiClient.put).mockResolvedValue({status: 200});
    const requestData = {someData: 'someData'};

    await useApi().put(testUrl, requestData);
    expect(apiClient.put).toHaveBeenCalledWith(testUrl, requestData);
    expect(useToastService().showSuccess).toHaveBeenCalledWith(`Successfully updated ${testUrl}!`);
  });

  it('should return get result and show success on axios code 200', async () => {
    const responseData = {someData: 'someData'};
    vi.mocked(apiClient.get).mockResolvedValue({status: 200, data: responseData});

    await expect(useApi().get(testUrl, {query: {id: '123', filter: 'xxx'}})).resolves.toEqual(responseData);
    const urlWithParams = `${testUrl}?id=123&filter=xxx`;
    expect(apiClient.get).toHaveBeenCalledWith(urlWithParams);
  });

  it.each([useApi().post, useApi().get]
  )('should error with Status 403', async (apiMethod) => {
    vi.mocked(apiClient.post).mockResolvedValue({status: 403});
    vi.mocked(apiClient.get).mockResolvedValue({status: 403});

    await apiMethod(testUrl, {});
    expect(useToastService().showError)
      .toHaveBeenCalledWith(expect.stringContaining('Failed to '), 'Error status code 403!');
  });
});
