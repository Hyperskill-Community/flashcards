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
// "id": "65a5677d296bf15455f0b868",
//   "question": "What is the root of this equation: 3x + 2 = 14",
//   "tags": [
//   "equations"
// ],
//   "type": "scq",
//   "createdAt": "2024-01-15T17:12:29.758Z",
//   "actions": [],
//   "options": [
//   "5",
//   "9",
//   "3",
//   "4"
// ],
//   "correctOption": 3
