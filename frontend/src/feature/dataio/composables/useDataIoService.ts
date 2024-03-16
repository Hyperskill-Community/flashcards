import useApi from "@/shared/composables/useApi.ts";
import {UserExport} from "@/feature/dataio/model/export.ts";

const useDataIoService = () => {

  const getUserExport = async () => {
    return await useApi().get<UserExport>('/export');
  };

  const makeDownload = (filename: string, blob: Blob) => {
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = filename;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  return {
    getUserExport,
    makeDownload
  };
};

export default useDataIoService;
