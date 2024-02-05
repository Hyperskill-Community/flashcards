export enum CardType {
  SIMPLEQA = 'qna',
  SINGLE_CHOICE = 'scq',
  MULTIPLE_CHOICE = 'mcq'
}

export type CardItem = {
  id: string,
  title: string,
  type: CardType,
}

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
}

export type CardResponse = {
  cards: CardItem[],
  currentPage: number,
  isLast: boolean,
}
