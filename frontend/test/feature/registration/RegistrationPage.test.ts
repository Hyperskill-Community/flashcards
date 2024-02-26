import {mount} from "@vue/test-utils";
import {mountOptions} from "../../util/useTestPlugins.ts";
import useRegistrationService from "@/feature/registration/composables/useRegistrationService.ts";
import App from "@/App.vue";
import RegistrationPage from "@/feature/registration/pages/RegistrationPage.vue";

vi.mock('@/feature/registration/composables/useRegistrationService.ts', () => {
  return {
    default: vi.fn().mockReturnValue({
      postNewUser: vi.fn(),
    })
  };
});

describe('RegistrationPage', () => {

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('should call post with input field contents', async () => {
    const email = 'email@t.com';
    const password = 'password';
    const wrapper = mount(RegistrationPage, mountOptions);
    await wrapper.findAllComponents('.v-text-field')?.at(0)?.setValue(email);
    await wrapper.findAllComponents('.v-text-field')?.at(1)?.setValue(password);
    await wrapper.findComponent('.v-btn').trigger('click');
    expect(useRegistrationService().postNewUser).toHaveBeenCalledWith(email, password);
  });

  it('should be mountable inside app', async () => {
    const app = mount(App, mountOptions);
    mount(RegistrationPage, {
      ...mountOptions,
      attachTo: app.vm.$el,
    });
    expect(app.text()).not.toContain('Error');
    expect(app.text()).toContain('Register new user');
    expect(app.text()).toContain('Welcome');
  });
});
