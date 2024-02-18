import {useErrorService} from '@/shared/composables/errorService';
import {useToastService} from "@/shared/composables/toastService";
import {expect, it, vi} from "vitest";

// create a mock for the toastService
const mockedToastService = vi.hoisted(() => {
    return {
        useToastService: vi.fn().mockReturnValue({
            showError: vi.fn()
        }),};
});
// mock the actual toastService composable using above mock
vi.mock('@/shared/composables/toastService', () => {
    return {
        useToastService: mockedToastService.useToastService,};
});

it('should handle and throw errors correctly', () => {
    const error = new Error('Test Error');
    const axiosError = {
        isAxiosError: true,
        response: {
            status: 404,
            data: {
                message: 'Not Found'
            }
        }
    };
    expect(() => useErrorService().handleAndThrow(axiosError)).toThrowError('Not Found');
    expect(useToastService().showError).toHaveBeenCalledWith('<b>Error status 404</b><br>Not Found');
    expect(() => useErrorService().handleAndThrow(error)).toThrowError('Unknown Error');
    expect(useToastService().showError).toHaveBeenCalledWith('<b>Unknown Error</b><br>Service Unavailable');
});
