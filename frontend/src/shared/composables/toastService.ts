import {ref} from "vue";

export enum ToastType {
  SUCCESS = 'success',
  ERROR = 'error',
  WARNING = 'warning'
}

export type Toast = {
  type: ToastType,
  header: string,
  title?: string,
  message: string,
};

export const toasts = ref(new Map<number, Toast>());

export const useToastService = () => {

  const showNotification = (type: ToastType, header: string, message: string, title?: string) => {
    const nextKey = () => toasts.value.size ? Math.max(...toasts.value.keys()) + 1 : 0;
    toasts.value.set(nextKey(), {type, header, title, message});
  };

  const showSuccess = (message: string, title?: string) => {
    showNotification(ToastType.SUCCESS, 'SUCCESS', message, title);
  };

  const showWarning = (message: string, title?: string) => {
    showNotification(ToastType.WARNING, 'WARNING', message, title);
  };

  const showError = (message: string, title?: string) => {
    showNotification(ToastType.ERROR, 'ERROR', message, title);
  };

  return {
    showSuccess,
    showWarning,
    showError,
  };
};
