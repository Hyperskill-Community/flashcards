import useApi from "@/shared/composables/useApi";

const useRegistrationService = () => {

  const postNewUser = async (email: string, password: string, errorResult: string = 'throw') => {
    const data = {
      email: email,
      password: password,
    };
    await useApi().post("/register", data, `User ${email} registered successfully!`,
      'Failed to register user');
  }

  return {
    postNewUser,
  }
}
export default useRegistrationService;
