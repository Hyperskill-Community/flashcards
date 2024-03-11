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
