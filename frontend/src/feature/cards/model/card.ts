export enum CardType {
  SIMPLEQA = 'sqa',
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
  question: string,
  options: string[],
  tags: string[],
}

export type CardResponse = {
  cards: CardItem[],
  currentPage: number,
  isLast: boolean,
}
