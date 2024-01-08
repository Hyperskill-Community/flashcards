import apiClient from '../../plugins/axios';
import {AxiosRequestConfig} from "axios";
import {useErrorService} from "@/shared/composables/errorService";
import {useToastService} from "@/shared/composables/toastService";

export function useAxiosTestService() {

  const testAxiosCall = async (method: string, errorResult: string = 'throw') => {
    const getUrl = 'https://jsonplaceholder.typicode.com/posts/1';
    const postUrl = 'https://jsonplaceholder.typicode.com/posts';
    const config: AxiosRequestConfig = {
      headers: {
        'test-header': 'test',
      },
    }
    const postData = {
      title: 'Test Post',
      body: 'This is a test post.',
      userId: 1,
    };

    try {
      const response =
        method === 'get' ? await apiClient.get(getUrl, config)
          : method === 'post' ? await apiClient.post(postUrl, postData, config)
            : false;

      if (!response) {
        throw new Error("Error!");
      }
      if (response && response.data) {
        console.log(response.data);
        useToastService().showSuccess('SUCCESS', `method ${method.toUpperCase()} works!`)
      }
    } catch (error: any) {
      errorResult === 'throw' ? useErrorService().handleAndThrow(error)
        : useErrorService().handleAndNotify('custom error', 'custom message');
    }
  }

  return {
    testAxiosCall
  }
}
