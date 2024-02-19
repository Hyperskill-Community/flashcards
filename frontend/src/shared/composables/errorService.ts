import {AxiosError} from "axios";
import {useToastService} from "@/shared/composables/toastService";

export type ErrorState = {
  code: string,
  message: string
};

export const useErrorService = () => {

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
    throw new Error(Object.values(newError).join(': '), {cause: error});
  };

  const handleAndNotify = (statusMessage: string, errorMessage: string) => {
    console.error(`${statusMessage} - ${errorMessage}`);
    useToastService().showError(`<b>${statusMessage}</b><br>${errorMessage}`);
  };

  return {
    handleAndThrow,
    handleAndNotify,
  };
};
