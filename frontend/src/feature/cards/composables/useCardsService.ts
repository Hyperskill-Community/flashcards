import apiClient from '@/plugins/axios';
import {useErrorService} from "@/shared/composables/errorService";
import {useToastService} from "@/shared/composables/toastService";
import apiUrl from "@/shared/composables/baseUrl";
import {Card} from "@/feature/cards/model/card";

const useCardsService = () => {

  const postNewUser = async (email: string, password: string, errorResult: string = 'throw') => {
    const postUrl = apiUrl + 'register';

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

  const getCards = async (categoryId: string, page: number, errorResult: string = 'throw') : Promise<Card[]> => {
    const url = `${apiUrl}cards?categoryId=${categoryId}&page=${page}`;

    try {
      const response = await apiClient.get(url);
      if (response.status !== 200) {
        throw new Error(`Error status code ${response.status}!`);
      } else {
        return (response.data.cards as Card[]);
      }
    } catch (error: any) {
      errorResult === 'throw' ? useErrorService().handleAndThrow(error)
        : useErrorService().handleAndNotify('custom error', 'custom message');
      return [];
    }
  }

  const getCardById = async (cardId: string, categoryId: string, errorResult: string = 'throw'): Promise<Card | null> => {
    const url = apiUrl + 'cards/' + cardId + '?categoryId=' + categoryId;
    try {
      const response = await apiClient.get(url);
      if (response.status !== 200) {
        throw new Error(`Error status code ${response.status}!`);
      } else {
        return (response.data as Card);
      }
    } catch (error: any) {
      errorResult === 'throw' ? useErrorService().handleAndThrow(error)
        : useErrorService().handleAndNotify('custom error', 'custom message');
      return null;
    }
  }

  const getCardCount = async (categoryId: string, errorResult: string = 'throw') => {
    const url = apiUrl + 'cards/count?categoryId=' + categoryId;

    try {
      const response = await apiClient.get(url);
      if (response.status !== 200) {
        throw new Error(`Error status code ${response.status}!`);
      } else {
        return response.data as number;
      }
    } catch (error: any) {
      errorResult === 'throw' ? useErrorService().handleAndThrow(error)
        : useErrorService().handleAndNotify('custom error', 'custom message');
      return 0;
    }
  }

  return {
    postNewUser,
    getCards,
    getCardById,
    getCardCount
  }
}
export default useCardsService;
