import {mount} from "@vue/test-utils";
import {mountOptions} from "../../util/useTestPlugins.ts";
import useRegistrationService from "@/feature/registration/composables/useRegistrationService.ts";
import App from "@/App.vue";
import RegistrationPage from "@/feature/registration/pages/RegistrationPage.vue";
import {nextTick} from "vue";

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

  it.each([{email: 'email@com', password: 'password'},
    {email: '@t.com', password: 'password'}, {email: 'email@t.', password: 'password'},
    {email: '', password: 'password'}, {email: 'email@valid.com', password: ''},
    {email: 'email@valid.com', password: '1234567'}]
  )('should not validate on invalid input', async ({email, password}) => {
    await fillFormAndTrigger(email, password);
    expect(useRegistrationService().postNewUser).not.toHaveBeenCalled();
  });

  it('should call post with input field contents', async () => {
    const email = 'email@t.com';
    const password = 'password';
    await fillFormAndTrigger(email, password);
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
  });

  const fillFormAndTrigger = async (email: string, password: string) => {
    const wrapper = mount(RegistrationPage, mountOptions);
    await wrapper.findAllComponents('.v-text-field')?.at(0)?.setValue(email);
    await wrapper.findAllComponents('.v-text-field')?.at(1)?.setValue(password);
    await nextTick(); // wait for v-form to validate and enable button
    await wrapper.findComponent('.v-btn').trigger('click');
  };
});
