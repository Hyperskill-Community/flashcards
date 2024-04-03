export enum CardType {
  SIMPLEQA = 'QNA',
  SINGLE_CHOICE = 'SCQ',
  MULTIPLE_CHOICE = 'MCQ'
}

export type CardItem = {
  id: string,
  question: string,
  type: CardType,
};

export type Card = {
  id?: string,
  type: CardType,
  tags: string[],
  question: string,
  title?: string,
  correctOption?: number,
  correctOptions?: number[],
  answer?: string,
  options?: string[],
};

export type CardPage = {
  cards: CardItem[],
  currentPage: number,
  isLast: boolean,
};

export const clone = (card: Card) : Card => {
  return {
    id: card.id,
    title: card.title,
    question: card.question,
    tags: [...card.tags],
    answer: card.answer,
    options: card.options && [...card.options],
    correctOption: card.correctOption,
    correctOptions: card.correctOptions && [...card.correctOptions],
    type: card.type
  };
};

export const generateNewCard = (cardType: CardType) : Card =>{
  const card = {} as Card;

  // shared fields of all types of card
  card.tags = [];
  card.title = '';
  card.question = '';

  switch (cardType) {
    case CardType.SIMPLEQA:
      card.type = CardType.SIMPLEQA;
      card.answer = 'New card';
      break;
    case CardType.MULTIPLE_CHOICE:
      card.type = CardType.MULTIPLE_CHOICE;
      card.correctOptions = [0];
      card.options = ['choice 1'];
      break;
    case CardType.SINGLE_CHOICE:
      card.type = CardType.SINGLE_CHOICE;
      card.options = ['choice 1'];
      card.correctOption = 0;
      break;
    default:
      break;
  }
  return card;
};

export const translateType = (type: CardType) : string => {
  switch (type) {
    case CardType.SIMPLEQA:
      return 'Simple Q&A';
    case CardType.SINGLE_CHOICE:
      return 'Single Choice';
    case CardType.MULTIPLE_CHOICE:
      return 'Multiple Choice';
  }
};
