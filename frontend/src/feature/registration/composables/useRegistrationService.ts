import useApi from "@/shared/composables/useApi";

const useRegistrationService = () => {

  const postNewUser = async (email: string, password: string) => {
    const data = {
      email: email,
      password: password,
    };
    await useApi().post("/register", data,
      {successMessage: `User ${email} registered successfully!`, errorMessage: 'Failed to register user'});
  };

  return {
    postNewUser,
  };
};
export default useRegistrationService;
