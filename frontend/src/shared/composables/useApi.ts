import apiClient from '@/plugins/axios';
import {useErrorService} from "@/shared/composables/errorService";
import {useToastService} from "@/shared/composables/toastService";


const useApi = () => {

  interface ApiOptions {
    successMessage?: string;
    errorMessage?: string;
    query?: Record<string, string>;
  }

  const joinUrlAndQueryParams = (url: string, query?: Record<string, string>) => {
    return query
      ? `${url}?${Object.keys(query).map(key => key + '=' + query[key]).join('&')}`
      : url;
  };

  return {
    post: async <R>(url: string, requestData: R, options: ApiOptions = {}) => {
      const urlWithParams = joinUrlAndQueryParams(url, options.query);
      const errorMessage = options.errorMessage ?? 'Failed to post';
      try {
        const response = await apiClient.post(urlWithParams, requestData);
        if (response.status !== 200 && response.status !== 201) {
          useErrorService().handleAndNotify(`Error status code ${response.status}!`, errorMessage);
        } else {
          useToastService().showSuccess(options.successMessage ?? `Successfully posted to ${url}!`);
        }
      } catch (error: any) {
        useErrorService().handleAndThrow(error, errorMessage);
      }
    },

    get: async <R>(url: string, options: ApiOptions = {}): Promise<R> => {
      const urlWithParams = joinUrlAndQueryParams(url, options.query);
      const errorMessage = options.errorMessage ?? 'Failed to load ${urlWithParams}';
      try {
        const response = await apiClient.get(urlWithParams);
        if (response.status !== 200) {
          useErrorService().handleAndNotify(`Error status code ${response.status}!`, errorMessage);
          return {} as R;
        } else {
          return response.data;
        }
      } catch (error: any) {
        useErrorService().handleAndThrow(error, errorMessage);
        return {} as R;
      }
    },

    put: async <R, S>(url: string, requestData: R, options: ApiOptions = {}): Promise<S> => {
      const urlWithParams = joinUrlAndQueryParams(url, options.query);
      const errorMessage = options.errorMessage ?? 'Failed to update';
      try {
        const response = await apiClient.put(urlWithParams, requestData);
        if (response.status != 200) {
          useErrorService().handleAndNotify(`Error status code ${response.status}!`, errorMessage);
          return {} as S;
        } else {
          useToastService().showSuccess(options.successMessage ?? `Successfully updated ${url}!`);
          return response.data;
        }
      } catch (error: any) {
        useErrorService().handleAndThrow(error, errorMessage);
        return {} as S;
      }
    },

    delete: async (url: string, options: ApiOptions = {}) => {
      const urlWithParams = joinUrlAndQueryParams(url, options.query);
      const errorMessage = options.errorMessage ?? 'Failed to delete';
      try {
        const response = await apiClient.delete(urlWithParams);
        if (response.status != 200) {
          useErrorService().handleAndNotify(`Error status code ${response.status}!`, errorMessage);
        } else {
          useToastService().showSuccess(options.successMessage ?? `Successfully deleted ${url}!`);
        }
      } catch (error: any) {
        useErrorService().handleAndThrow(error, errorMessage);
      }
    }
  };
};
export default useApi;
