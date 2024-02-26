import {mount} from "@vue/test-utils";
import ToastNotification from "@/shared/notifications/ToastNotification.vue";
import {Toast, toasts, ToastType} from "@/shared/composables/toastService";

describe('ToastNotification.vue', () => {

  it('renders notification header and message', async () => {
    const wrapper = mount(ToastNotification, {
      props: {
        notificationKey: 1,
        notification: {type: ToastType.SUCCESS, header: 'Header', message: 'Message'}
      }
    });

    expect(wrapper.text()).toContain('Header');
    expect(wrapper.text()).toContain('Message');
  });

  it('removes notification after timeout', async () => {
    vi.useFakeTimers();
    toasts.value.set(1, {type: ToastType.SUCCESS, header: 'Header', message: 'Message'});

    mount(ToastNotification, {
      props: {
        notificationKey: 1,
        notification: toasts.value.get(1) as Toast
      }
    });

    expect(toasts.value.has(1)).toBe(true);
    vi.advanceTimersByTime(5000);
    expect(toasts.value.has(1)).toBe(false);
    vi.useRealTimers();
  });

  it.each([[ToastType.SUCCESS, 'success'], [ToastType.ERROR, 'error'], [ToastType.WARNING, 'warning']]
  )('applies correct class for %s type', (type, clazz) => {
    const wrapper = mount(ToastNotification, {
      props: {
        notificationKey: 1,
        notification: {type: type, header: 'Header', message: 'Message'}
      }
    });

    expect(wrapper.get(`.${clazz}`)).toBeTruthy();
  });

});
