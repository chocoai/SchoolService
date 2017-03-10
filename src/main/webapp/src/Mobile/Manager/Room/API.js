import * as Mobile from '../../Mobile.js'
import * as Semester from '../Semester/API.js'
import * as Course from '../Course/API.js'
export const base = 'room'
const base3 = 'courseRoom'
export const checkNameForAdd = `${Mobile.Prefix}/${base}/checkNameForAdd`
export const checkNameForEdit = `${Mobile.Prefix}/${base}/checkNameForEdit`
export const save = `${Mobile.Prefix}/${base}/save`
export const edit = `${Mobile.Prefix}/${base}/edit`
export const active = `${Mobile.Prefix}/${base}/active`
export const inactive = `${Mobile.Prefix}/${base}/inactive`
export const get = `${Mobile.Prefix}/${base}/get`
export const query = `${Mobile.Prefix}/${base}/query`
export const total = `${Mobile.Prefix}/${base}/total`
export const getCourse = `${Mobile.Prefix}/${base3}/getCourse`
export const getSemester = `${Semester.getNows}`
export const getCourseList = `${Course.list}`
export const getCourseAll = `${Course.all}`
