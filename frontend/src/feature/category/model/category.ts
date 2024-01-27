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
