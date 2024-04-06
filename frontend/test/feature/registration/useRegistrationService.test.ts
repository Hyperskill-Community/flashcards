import useApi from "@/shared/composables/useApi";
import useRegistrationService from "@/feature/registration/composables/useRegistrationService.ts";

vi.mock('@/shared/composables/useApi', () => {
  return {
    default: vi.fn().mockReturnValue({
      post: vi.fn(),
    })};
});

describe('useRegistrationService', () => {

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('should call useApi().post with the correct data', async () => {
    const email = 'email@test.com';
    const password = 'password';
    await useRegistrationService().postNewUser(email, password);
    expect(useApi().post).toHaveBeenCalledWith('/register', {email, password},
      {successMessage: `User ${email} registered successfully!`, errorMessage: 'Failed to register user'});
  });
});
