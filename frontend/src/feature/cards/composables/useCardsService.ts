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

  const putCard = async (cardId: string, categoryId: string, data: Card) : Promise<Card>  => {
    return await useApi().put(`${ENDPOINT}/${cardId}`, data, {categoryId: categoryId}, `Card ${data.question} successfully updated!`);
  };

  return {
    getCards,
    getCardById,
    getCardCount,
    putCard
  };
};
export default useCardsService;
