import { request } from '@/utils/request'

export const getInventoryData = (data) => {
  return request({
    url: '/api/statistics/getInventoryData',
    method: 'post',
    data
  })
}
