import {toasts, ToastType, useToastService} from "@/shared/composables/toastService";
import {describe, expect, it} from "vitest";

describe('toastService', () => {
  it('should show success message', () => {
    useToastService().showSuccess('Success message');
    expect(toasts.value.size).toBe(1);
    const toast = toasts.value.values().next().value;
    expect(toast.type).toBe(ToastType.SUCCESS);
    expect(toast.header).toBe('SUCCESS');
    expect(toast.message).toBe('Success message');
  });

  it('should show warning message', () => {
    toasts.value.clear();
    useToastService().showWarning('Warning message');
    expect(toasts.value.size).toBe(1);
    const toast = toasts.value.values().next().value;
    expect(toast.type).toBe(ToastType.WARNING);
    expect(toast.header).toBe('WARNING');
    expect(toast.message).toBe('Warning message');
  });

  it('should show error message', () => {
    toasts.value.clear();
    useToastService().showError('Error message');
    expect(toasts.value.size).toBe(1);
    const toast = toasts.value.values().next().value;
    expect(toast.type).toBe(ToastType.ERROR);
    expect(toast.header).toBe('ERROR');
    expect(toast.message).toBe('Error message');
  });
});
