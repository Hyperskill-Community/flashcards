import {Card, CardPage} from "@/feature/cards/model/card";
import useApi from "@/shared/composables/useApi";

const useCardsService = () => {

  const ENDPOINT = '/cards';

  const getCards = async (categoryId: string, titleFilter: string, page: number) => {
    return await useApi().get<CardPage>(ENDPOINT,
      {categoryId: categoryId, page: String(page), titleFilter: titleFilter});
  };

  const getCardById = async (cardId: string, categoryId: string) => {
    return await useApi().get<Card>(`${ENDPOINT}/${cardId}`, {categoryId: categoryId});
  };

  const getCardCount = async (categoryId: string) => {
    return await useApi().get<number>(`${ENDPOINT}/count`, {categoryId: categoryId});
  };

  return {
    getCards,
    getCardById,
    getCardCount
  }
}
export default useCardsService;
