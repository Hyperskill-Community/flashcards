export type Action = {
  action: string,
  uri: string
}

export type Category = {
  id: string,
  name: string,
  actions: Action[],
  numberOfCards?: number,
  description?: string,
}

export const getAccess = (category: Category): string => {
  return category.actions.map(item => item.action).join(', ');
}
