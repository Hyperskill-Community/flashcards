import apiClient from '@/plugins/axios';
import {useErrorService} from "@/shared/composables/errorService";
import {useToastService} from "@/shared/composables/toastService";

const useCardsService = () => {

  const postNewUser = async (email: string, password: string, errorResult: string = 'throw') => {
    const postUrl = 'http://127.0.0.1:8080/api/register';

    const postData = {
      email: email,
      password: password,
    };

    try {
      const response = await apiClient.post(postUrl, postData);
      if (response.status !== 200) {
        throw new Error(`Error status code ${response.status}!`);
      } else {
        useToastService().showSuccess('SUCCESS', `User ${email} registered successfully!`)
      }
    } catch (error: any) {
      errorResult === 'throw' ? useErrorService().handleAndThrow(error)
        : useErrorService().handleAndNotify('custom error', 'custom message');
    }
  }

  return {
    postNewUser
  }
}
export default useCardsService;
