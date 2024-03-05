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
  type: string,
  tags: string[],
  question: string,
  title?: string,
  correctOption?: string,
  correctOptions?: string[],
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
    title: card.title,
    question: card.question,
    // copy from scq.tags array and fill up to 4 elements with empty strings
    tags: [...card.tags],
    answer: card.answer,
    options: card.options && [...card.options],
    correctOption: card.correctOption,
    correctOptions: card.correctOptions && [...card.correctOptions],
    type: card.type
  };
};
