import useApi from "@/shared/composables/useApi";
import useCardsService from "@/feature/cards/composables/useCardsService.ts";
import {Card, CardItem, CardPage, CardType} from "@/feature/cards/model/card.ts";

vi.mock('@/shared/composables/useApi', () => {
  return {
    default: vi.fn().mockReturnValue({
      get: vi.fn(),
      post: vi.fn(),
      put: vi.fn(),
      delete: vi.fn(),
    })
  };
});

describe('useCardsService', () => {

  const testCard: CardItem = {
    id: '1',
    question: 'Test question',
    type: CardType.SIMPLEQA,
  };

  const otherCard: CardItem = {
    id: '2',
    question: 'Other question',
    type: CardType.MULTIPLE_CHOICE,
  };

  const page: CardPage = {
    cards: [testCard, otherCard],
    currentPage: 1,
    isLast: true
  };

  const card : Card = {
    id: '1',
    type: CardType.SIMPLEQA,
    tags: [],
    question: 'question'
  };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('getCards should return cards in page', async () => {
    vi.mocked(useApi().get).mockResolvedValue(page);
    await expect(useCardsService().getCards('catid','filter', 0)).resolves.toEqual(page);
    expect(useApi().get).toHaveBeenCalledWith('/cards',
      {query: {categoryId: 'catid', page: '0', titleFilter: 'filter'}});
  });

  it('getCardById should return category by id', async () => {
    vi.mocked(useApi().get).mockResolvedValue(testCard);
    await expect(useCardsService().getCardById('1', 'catid')).resolves.toEqual(testCard);
    expect(useApi().get).toHaveBeenCalledWith('/cards/1', {query: {categoryId: 'catid',}});
  });


  it('getCardCount should return # of cards', async () => {
    vi.mocked(useApi().get).mockResolvedValue(15);
    await expect(useCardsService().getCardCount('catid')).resolves.toEqual(15);
    expect(useApi().get).toHaveBeenCalledWith('/cards/count', {query: {categoryId: 'catid',}});
  });

  it('postNewCard should return card created', async () => {
    await useCardsService().postNewCard('catid', card);
    expect(useApi().post).toHaveBeenCalledWith('/cards', card,
      {successMessage: 'Card question successfully created!', query: {categoryId: 'catid'}});
  });


  it('putCard should return card updated', async () => {
    await useCardsService().putCard('catid', card);
    expect(useApi().put).toHaveBeenCalledWith('/cards/1', card,
      {successMessage: 'Card question successfully updated!', query: {categoryId: 'catid'}});
  });

  it('deleteCard should return card deleted', async () => {
    await useCardsService().deleteCard('catid', card);
    expect(useApi().delete).toHaveBeenCalledWith('/cards/1',
      {successMessage: 'Card question successfully deleted!', query: {categoryId: 'catid'}});
  });

});
