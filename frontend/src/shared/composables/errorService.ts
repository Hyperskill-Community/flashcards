import {useToastService} from "@/shared/composables/toastService";

export type ErrorState = {
  code: string,
  message: string
};

export const useErrorService = () => {

  const handleError = (error: any, message?: string): ErrorState => {
    const errorState = {} as ErrorState;
    errorState.code = (error.isAxiosError && error.response)
      ? `Error status ${error.response.status}`
      : error.message ?? 'Unknown Error';
    errorState.message = (error.isAxiosError && error.response?.data)
      ? error.response.data.message
      : message ?? 'Service Unavailable';
    return errorState;
  };

  const handleAndThrow = (error: any, message?: string) => {
    const newError = handleError(error, message);
    useToastService().showError(newError.message, newError.code);
    throw new Error(Object.values(newError).join(': '), {cause: error});
  };

  const handleAndNotify = (statusMessage: string, errorMessage: string) => {
    console.error(`${statusMessage} - ${errorMessage}`);
    useToastService().showError(errorMessage, statusMessage);
  };

  return {
    handleAndThrow,
    handleAndNotify,
  };
};
