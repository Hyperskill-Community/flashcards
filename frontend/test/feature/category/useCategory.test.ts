import {ActionType, Category} from "@/feature/category/model/category.ts";
import {getAccess, getDeleteUri, getPutUri} from "@/feature/category/composables/useCategory.ts";

describe('useCategory', () => {
  const writeCat: Category = {
    id: '1',
    name: 'Test Category',
    description: 'Test Description',
    actions: [{action: ActionType.READ, uri: '/read'}, {action: ActionType.WRITE, uri: '/write'}]
  };

  const deleteCat: Category = {
    id: '2',
    name: 'Other Category',
    description: 'Other Description',
    actions: [{action: ActionType.DELETE, uri: '/delete'}]
  };

  it('getAccess should return actions separated by comma', () => {
    expect(getAccess(writeCat)).toBe('READ, WRITE');
  });

  it('getPutUri should return uri for write action', () => {
    expect(getPutUri(writeCat)).toBe('/write');
  });

  it('getPutUri should return undefined for delete action', () => {
    expect(getPutUri(deleteCat)).toBe(undefined);
  });

  it('getDeleteUri should return uri for delete action', () => {
    expect(getDeleteUri(deleteCat)).toBe('/delete');
  });

  it('getDeleteUri should return undefined for write action', () => {
    expect(getDeleteUri(writeCat)).toBe(undefined);
  });

});

