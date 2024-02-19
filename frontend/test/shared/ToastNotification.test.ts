import ToastNotification from "@/shared/notifications/ToastNotification.vue";
import {Toast, toasts, ToastType} from "@/shared/composables/toastService";
import {mount} from "@vue/test-utils";

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

  it('applies correct class for success type', () => {
    const wrapper = mount(ToastNotification, {
      props: {
        notificationKey: 1,
        notification: {type: ToastType.SUCCESS, header: 'Header', message: 'Message'}
      }
    });

    expect(wrapper.get('.success')).toBeTruthy();
  });

  it('applies correct class for error type', () => {
    const wrapper = mount(ToastNotification, {
      props: {
        notificationKey: 1,
        notification: {type: ToastType.ERROR, header: 'Header', message: 'Error message'}
      }
    });

    expect(wrapper.text()).contains('Error message');
    expect(wrapper.get('.error')).toBeTruthy();
  });

  it('applies correct class for warning type', () => {
    const wrapper = mount(ToastNotification, {
      props: {
        notificationKey: 1,
        notification: {type: ToastType.WARNING, header: 'Header', message: 'Message'}
      }
    });

    expect(wrapper.get('.warning')).toBeTruthy();
  });
});
