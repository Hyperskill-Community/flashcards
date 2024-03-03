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
  id: string,
  title: string,
  correctOption: string,
  correctOptions: string[],
  answer: string,
  question: string,
  options: string[],
  tags: string[],
  type: string,
};

export type CardPage = {
  cards: CardItem[],
  currentPage: number,
  isLast: boolean,
};
