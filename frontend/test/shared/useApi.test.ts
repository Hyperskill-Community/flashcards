import apiClient from "@/plugins/axios.ts";
import useApi from "@/shared/composables/useApi.ts";
import {useToastService} from "@/shared/composables/toastService.ts";
import {expect, vi} from "vitest";

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

  it('should show Service unavailable and throw if Axios throws', async () => {
    vi.mocked(apiClient.delete).mockRejectedValue(new Error('Test Error'));
    await expect(useApi().delete(testUrl)).rejects.toThrowError('Unknown Error: Service Unavailable');

    expect(apiClient.delete).toHaveBeenCalledWith(testUrl);
    expect(useToastService().showError).toHaveBeenCalledWith('Service Unavailable', 'Unknown Error');
  });

  it('should show Custom Error no throw if Axios throws', async () => {
    vi.mocked(apiClient.delete).mockRejectedValue(new Error('Test Error'));

    await useApi().delete(testUrl, '', '', {code: 'Error 999', message: 'No Service'});
    expect(apiClient.delete).toHaveBeenCalledWith(testUrl);
    expect(useToastService().showError).toHaveBeenCalledWith('No Service', 'Error 999');
  });

  it.each(
    [undefined, 'Congrats - you deleted it!']
  )('should show %s on delete with status 200', async (customSuccess) => {
    vi.mocked(apiClient.delete).mockResolvedValue({status: 200});
    const successMessage = customSuccess?? `Successfully deleted ${testUrl}!`;

    await useApi().delete(testUrl, customSuccess);
    expect(apiClient.delete).toHaveBeenCalledWith(testUrl);
    expect(useToastService().showSuccess).toHaveBeenCalledWith(successMessage);
  });

  it.each([undefined, 'Error - cannot delete that!']
  )('should show %s on delete with Status 403', async (customError) => {
    vi.mocked(apiClient.delete).mockResolvedValue({status: 403});
    const errorMessage = customError?? 'Failed to delete';

    await useApi().delete(testUrl, '', customError);
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

    await expect(useApi().get(testUrl, {id: '123', filter: 'xxx'})).resolves.toEqual(responseData);
    const urlWithParams = `${testUrl}?id=123&filter=xxx`;
    expect(apiClient.get).toHaveBeenCalledWith(urlWithParams);
  });

  it.each([useApi().post, useApi().put, useApi().get]
  )('should error with Status 403', async (apiMethod) => {
    vi.mocked(apiClient.post).mockResolvedValue({status: 403});
    vi.mocked(apiClient.put).mockResolvedValue({status: 403});
    vi.mocked(apiClient.get).mockResolvedValue({status: 403});

    await apiMethod(testUrl, {});
    expect(useToastService().showError)
      .toHaveBeenCalledWith(expect.stringContaining('Failed to '), 'Error status code 403!');
  });
});
