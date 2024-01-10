import {reactive} from 'vue';
import {AxiosError} from "axios";
import {useToastService} from "@/shared/composables/toastService";

export function useErrorService() {

  const errorState = reactive({
    errorCode: '',
    errorMessage: '',
  });

  const handleError = (error: AxiosError | any, message?: string) => {
    errorState.errorCode = (error.isAxiosError && error.response) ? `Error status ${error.response.status}` : 'Unknown Error';
    errorState.errorMessage = (error.isAxiosError && error.response?.data) ? error.response.data.message : message || 'Service Unavailable';
    return errorState;
  }

  const handleAndThrow = (error: AxiosError | any, message?: string) => {
    const newError = handleError(error, message);
    useToastService().showError('ERROR', `<b>${newError.errorCode}</b><br>${newError.errorMessage}`);
    throw new Error(Object.values(newError).filter(i => i).join(': '), {cause: error});
  }

  const handleAndNotify = (error: AxiosError | any, message?: string) => {
    const newError = handleError(error, message);
    console.error(`${newError.errorCode} - ${newError.errorMessage}`);
    useToastService().showError('ERROR', `<b>${error}</b><br>${message}`);
  }

  return {
    handleError,
    handleAndThrow,
    handleAndNotify,
  };
}
