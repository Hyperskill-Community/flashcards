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
  question: string,
  title?: string,
  correctOption?: string,
  correctOptions?: string[],
  answer?: string,
  options?: string[],
  tags: string[],
};

export type CardPage = {
  cards: CardItem[],
  currentPage: number,
  isLast: boolean,
};

export const cloneQna = (qna: Card) : Card => {
  return {
    title: qna.title,
    question: qna.question,
    // copy from scq.tags array and fill up to 4 elements with empty strings
    tags: qna.tags.concat(Array(4 - qna.tags.length).fill('')),
    answer: qna.answer,
    type: CardType.SIMPLEQA
  };
};
