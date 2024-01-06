import {ref} from "vue";

export enum ToastType {
  SUCCESS = 'success',
  ERROR = 'error',
  WARNING = 'warning'
}

export type Toast = {
  type: ToastType,
  header: string,
  message: string,
}

export const toasts = ref(new Map<number, Toast>());

export function useToastService() {

  const showNotification = (type: ToastType, header: string, message: string) => {
    toasts.value.set(Math.round(Math.random() * 12345), {type, header, message});
  }

  const showSuccess = (header: string, message: string) => {
    showNotification(ToastType.SUCCESS, header, message);
  }

  const showWarning = (header: string, message: string) => {
    showNotification(ToastType.WARNING, header, message);
  }

  const showError = (header: string, message: string) => {
    showNotification(ToastType.ERROR, header, message);
  }

  return {
    showSuccess,
    showWarning,
    showError,
  }
}
