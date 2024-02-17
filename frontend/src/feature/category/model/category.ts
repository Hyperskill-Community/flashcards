export enum ActionType {
  READ = 'READ',
  WRITE = 'WRITE',
  DELETE = 'DELETE'
}

export type Action = {
  action: ActionType,
  uri: string
}

export type Category = {
  id: string,
  name: string,
  actions: Action[],
  numberOfCards?: number,
  description?: string,
}

export type CategoryPage = {
  categories: Category[],
  currentPage: number,
  isLast: boolean,
}

export interface CategoryRequest {
  name: string;
  description: string;
}
