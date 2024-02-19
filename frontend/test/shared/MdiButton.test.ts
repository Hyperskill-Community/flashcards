import EditMdiButton from '@/shared/components/EditMdiButton.vue';
import DeleteMdiButton from '@/shared/components/DeleteMdiButton.vue';
import SubmitMdiButton from '@/shared/components/SubmitMdiButton.vue';
import BackMdiButton from '@/shared/components/BackMdiButton.vue';
import OpenMdiButton from '@/shared/components/OpenMdiButton.vue';
import AddMdiButton from '@/shared/components/AddMdiButton.vue';
import {mount} from '@vue/test-utils';
import {mountOptions} from "../util/useTestPlugins.ts";

describe('MdiButtons', () => {

  it.each([AddMdiButton, BackMdiButton, DeleteMdiButton, EditMdiButton, OpenMdiButton, SubmitMdiButton]
  )('should emit click', async (MdiButton) => {
    const wrapper = mount(MdiButton, {
      ...mountOptions,
      props: {
        disabled: false,
        clickHandler: vi.fn(),
      }
    });

    await wrapper.find('button').trigger('click');
    expect(wrapper.emitted().click).toBeTruthy();
  });

  it.each([AddMdiButton, BackMdiButton, DeleteMdiButton, EditMdiButton, OpenMdiButton, SubmitMdiButton]
  )('should not emit click event when button is clicked and disabled', async (MdiButton) => {
    const wrapper = mount(MdiButton, {
      ...mountOptions,
      props: {
        disabled: true,
        clickHandler: vi.fn(),
      }
    });

    await wrapper.find('button').trigger('click');
    expect(wrapper.emitted().click).toBeFalsy();
  });

  it.each([AddMdiButton, BackMdiButton, DeleteMdiButton, EditMdiButton, OpenMdiButton, SubmitMdiButton]
  )('should display tooltipText provided', async (MdiButton) => {
    const wrapper = mount(MdiButton, {
      ...mountOptions,
      props: {
        disabled: false,
        clickHandler: vi.fn(),
        tooltipText: 'Test Tooltip'
      }
    });

    expect(wrapper.findComponent({ name: 'VTooltip' }).props().disabled).toBe(false);
    expect(wrapper.findComponent({ name: 'VTooltip' }).props().text).toBe('Test Tooltip');
  });

  it.each([AddMdiButton, BackMdiButton, DeleteMdiButton, EditMdiButton, OpenMdiButton]
  )('should not display tooltip when tooltipText is not provided', async (MdiButton) => {
    const wrapper = mount(MdiButton, {
      ...mountOptions,
      props: {
        disabled: false,
        clickHandler: vi.fn(),
      }
    });

    expect(wrapper.findComponent({ name: 'VTooltip' }).props().disabled).toBe(true);
  });

  it('submit should display submit tooltip when nothing provided', async () => {
    const wrapper = mount(SubmitMdiButton, {
      ...mountOptions,
      props: {
        disabled: false,
        clickHandler: vi.fn(),
      }
    });

    expect(wrapper.findComponent({ name: 'VTooltip' }).props().disabled).toBe(false);
    expect(wrapper.findComponent({ name: 'VTooltip' }).props().text).toBe('Submit');
  });
});
