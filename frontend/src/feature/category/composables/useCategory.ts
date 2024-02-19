import {ActionType, Category} from "@/feature/category/model/category";

export const getAccess = (category: Category): string => {
  return category.actions.map(item => item.action).join(', ');
};

export const getPutUri = (category: Category): string | undefined => {
  return category.actions.filter(item => item.action === ActionType.WRITE)[0]?.uri;
};

export const getDeleteUri = (category: Category): string | undefined => {
  return category.actions.filter(item => item.action === ActionType.DELETE)[0]?.uri;
};
