import {AxiosError} from "axios";
import {useToastService} from "@/shared/composables/toastService";

export type ErrorState = {
  code: string,
  message: string
};

export function useErrorService() {

  const handleError = (error: AxiosError | any, message?: string) : ErrorState => {
    const errorState = {} as ErrorState;
    errorState.code = (error.isAxiosError && error.response)
      ? `Error status ${error.response.status}`
      : 'Unknown Error';
    errorState.message = (error.isAxiosError && error.response?.data)
      ? error.response.data.message
      : message || 'Service Unavailable';
    return errorState;
  };

  const handleAndThrow = (error: AxiosError | any, message?: string) => {
    const newError = handleError(error, message);
    useToastService().showError(`<b>${newError.code}</b><br>${newError.message}`);
    throw new Error(Object.values(newError).filter(i => i).join(': '), {cause: error});
  };

  const handleAndNotify = (error: AxiosError | any, message?: string) => {
    const newError = handleError(error, message);
    console.error(`${newError.code} - ${newError.message}`);
    useToastService().showError(`<b>${error}</b><br>${message}`);
  };

  return {
    handleAndThrow,
    handleAndNotify,
  };
}
