import {Card, CardPage} from "@/feature/cards/model/card";
import useApi from "@/shared/composables/useApi";

const useCardsService = () => {

  const ENDPOINT = '/cards';

  const getCards = async (categoryId: string, titleFilter: string, page: number) => {
    return await useApi().get<CardPage>(ENDPOINT,
      {query: {categoryId: categoryId, page: String(page), titleFilter: titleFilter}});
  };

  const getCardById = async (cardId: string, categoryId: string) => {
    return await useApi().get<Card>(`${ENDPOINT}/${cardId}`, {query: {categoryId: categoryId}});
  };

  const getCardCount = async (categoryId: string) => {
    return await useApi().get<number>(`${ENDPOINT}/count`, {query: {categoryId: categoryId}});
  };

  const putCard = async (categoryId: string, data: Card): Promise<Card> => {
    return await useApi().put(`${ENDPOINT}/${data.id}`, data,
      {query: {categoryId: categoryId}, successMessage: `Card ${data.question} successfully updated!`});
  };

  const deleteCard = async (categoryId: string, data: Card) => {
    await useApi().delete(`${ENDPOINT}/${data.id}`,
      {query: {categoryId: categoryId}, successMessage: `Card ${data.question} successfully deleted!`});
  };

  const postNewCard = async (categoryId: string, data: Card) => {
    await useApi().post(`${ENDPOINT}`, data,
      {query: {categoryId: categoryId}, successMessage: `Card ${data.question} successfully created!`});
  };

  return {
    getCards,
    getCardById,
    getCardCount,
    putCard,
    deleteCard,
    postNewCard,
  };
};
export default useCardsService;
