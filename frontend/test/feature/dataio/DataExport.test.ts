import {mount} from "@vue/test-utils";
import DataExport from "@/feature/dataio/pages/DataExport.vue";
import {mountOptions} from "../../util/useTestPlugins.ts";
import useDataIoService from "@/feature/dataio/composables/useDataIoService.ts";
import {expect} from "vitest";

vi.mock("@/feature/dataio/composables/useDataIoService.ts", () => {
  return {
    default: vi.fn().mockReturnValue({
      getUserExport: vi.fn(),
      makeDownload: vi.fn(),
    })
  };
});

describe('DataExport', () => {

  it('should call getUserExport and makeDownload on export button click', async () => {
    const wrapper = mount(DataExport, mountOptions);
    const exportData = {username: 'user', categories: []};
    vi.mocked(useDataIoService().getUserExport).mockResolvedValue(exportData);
    await wrapper.findComponent('.v-btn').trigger('click');
    expect(useDataIoService().getUserExport).toHaveBeenCalled();
    expect(useDataIoService().makeDownload)
      .toHaveBeenCalledWith(
        expect.stringContaining('flashcards_user.json'),
        new Blob([JSON.stringify(exportData)], {type: 'application/json'})
      );
  });
});
