import useApi from "@/shared/composables/useApi";
import {UserExport} from "@/feature/dataio/model/export.ts";
import useDataIoService from "@/feature/dataio/composables/useDataIoService.ts";
import {mount} from "@vue/test-utils";
import {mountOptions} from "../../util/useTestPlugins.ts";
import DataExport from "@/feature/dataio/pages/DataExport.vue";

vi.mock('@/shared/composables/useApi', () => {
  return {
    default: vi.fn().mockReturnValue({
      get: vi.fn(),
    })};
});

describe('useDataIoService', () => {

  const exportData : UserExport = {
    username: 'user',
    categories: [
      {
        name: 'Test Category',
        description: 'Test',
        cards: [
          {question: 'question', id: '1' },
          {question: 'question 2', id: '2' },
        ]
      },
      {
        name: 'Other Category',
        description: 'More Test',
        cards: [
        ]
      }
    ],
  };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('getUserExport should call export endpoint and return data', async () => {
    vi.mocked(useApi().get).mockResolvedValue(exportData);
    await expect(useDataIoService().getUserExport()).resolves.toEqual(exportData);
    expect(useApi().get).toHaveBeenCalledWith('/export');
  });

  it('makeDownload should set link attributes and trigger link', () => {
    const filename = 'export.json';
    const blob = new Blob([JSON.stringify(exportData)], {type: 'application/json'});
    mount(DataExport, mountOptions);
    const link = document.createElement('a');
    const href = 'blob:http://localhost:8080/1234';
    global.URL.createObjectURL = vi.fn().mockReturnValue(href);
    vi.spyOn(document, 'createElement').mockImplementation(() => link);
    vi.spyOn(link, 'click');

    useDataIoService().makeDownload(filename, blob);
    expect(link.download).toBe(filename);
    expect(link.href).toBe(href);
    expect(link.click).toHaveBeenCalled();
  });
});
