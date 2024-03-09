import apiClient from '@/plugins/axios';
import {ErrorState, useErrorService} from "@/shared/composables/errorService";
import {useToastService} from "@/shared/composables/toastService";

const useApi = () => {

  const handleNonAxiosError = (error: any, customError?: ErrorState) => {
    customError
      ? useErrorService().handleAndNotify(customError.code, customError.message)
      : useErrorService().handleAndThrow(error);
  };

  return {
    post: async <R>(url: string, requestData: R, successMessage?: string,
                    errorMessage?: string, customError?: ErrorState) => {
      try {
        const response = await apiClient.post(url, requestData);
        if (response.status !== 200 && response.status !== 201) {
          useErrorService().handleAndNotify(
            `Error status code ${response.status}!`, errorMessage?? 'Failed to post');
        } else {
          useToastService().showSuccess(successMessage?? `Successfully posted to ${url}!`);
        }
      } catch (error: any) {
        handleNonAxiosError(error, customError);
      }
    },

    get: async <R>(url: string, query: Record<string, string> = {},
                   errorMessage?: string, customError?: ErrorState): Promise<R> => {
      const urlWithParams = `${url}?${Object.keys(query).map(key => key + '=' + query[key]).join('&')}`;

      try {
        const response = await apiClient.get(urlWithParams);
        if (response.status !== 200) {
          useErrorService().handleAndNotify(
            `Error status code ${response.status}!`, errorMessage?? `Failed to load ${urlWithParams}`);
          return {} as R;
        } else {
          return response.data;
        }
      } catch (error: any) {
        handleNonAxiosError(error, customError);
        return {} as R;
      }
    },

    put: async <R, S>(url: string, requestData: R, query?: Record<string, string>, successMessage?: string,
                   errorMessage?: string, customError?: ErrorState) : Promise<S> => {
      const urlWithParams = query ?
        `${url}?${Object.keys(query).map(key => key + '=' + query[key]).join('&')}` : url;
      try {
        const response = await apiClient.put(urlWithParams, requestData);
        if (response.status != 200) {
          useErrorService().handleAndNotify(
            `Error status code ${response.status}!`, errorMessage?? 'Failed to update');
          return {} as S;
        } else {
          useToastService().showSuccess(successMessage?? `Successfully updated ${url}!`);
          return response.data;
        }
      } catch (error: any) {
        handleNonAxiosError(error, customError);
        return {} as S;
      }
    },

    delete: async (url: string, successMessage?: string,
                   errorMessage?: string, customError?: ErrorState) => {
      try {
        const response = await apiClient.delete(url);
        if (response.status != 200) {
          useErrorService().handleAndNotify(
            `Error status code ${response.status}!`, errorMessage?? 'Failed to delete');
        } else {
          useToastService().showSuccess(successMessage?? `Successfully deleted ${url}!`);
        }
      } catch (error: any) {
        handleNonAxiosError(error, customError);
      }
    }
  };
};
export default useApi;
