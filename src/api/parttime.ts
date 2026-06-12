import request from '@/utils/request'

// 用户相关
export function login(data: { username: string; password: string }) {
  return request<UserInfo>({
    url: '/user/login',
    method: 'POST',
    data,
  })
}

export function register(data: { username: string; password: string; name: string; phone: string }) {
  return request<UserInfo>({
    url: '/user/register',
    method: 'POST',
    data,
  })
}

export function updateUser(data: { id: number; name?: string; phone?: string }) {
  return request<UserInfo>({
    url: '/user/update',
    method: 'PUT',
    data,
  })
}

export function getUserById(id: number) {
  return request<UserInfo>({
    url: `/user/${id}`,
    method: 'GET',
  })
}

// 兼职相关
export function getJobList(keyword?: string) {
  return request<Job[]>({
    url: '/job/list',
    method: 'GET',
    params: keyword ? { keyword } : {},
  })
}

export function getJobDetail(id: number) {
  return request<Job>({
    url: `/job/${id}`,
    method: 'GET',
  })
}

export function addJob(data: Omit<Job, 'id'>) {
  return request<null>({
    url: '/job/add',
    method: 'POST',
    data,
  })
}

export function updateJob(data: Job) {
  return request<null>({
    url: '/job/update',
    method: 'PUT',
    data,
  })
}

export function deleteJob(id: number) {
  return request<null>({
    url: `/job/${id}`,
    method: 'DELETE',
  })
}

// 申请相关
export function getApplyList(userId?: number) {
  return request<JobApply[]>({
    url: '/apply/list',
    method: 'GET',
    params: userId ? { userId } : {},
  })
}

export function addApply(data: { userId: number; jobId: number }) {
  return request<null>({
    url: '/apply/add',
    method: 'POST',
    data,
  })
}

export function updateApplyStatus(data: { id: number; status: string }) {
  return request<null>({
    url: '/apply/status',
    method: 'PUT',
    data,
  })
}

export function deleteApply(id: number) {
  return request<null>({
    url: `/apply/${id}`,
    method: 'DELETE',
  })
}

export interface UserInfo {
  id: number
  username: string
  password?: string
  name: string
  phone: string
  role: string
  avatar?: string
  menu?: string[]
}

export interface Job {
  id?: number
  title: string
  location: string
  salary: string
  workTime: string
  description: string
}

export interface JobApply {
  id: number
  userId: number
  jobId: number
  applyTime: string
  status: string
  jobTitle?: string
  jobLocation?: string
  jobSalary?: string
}
