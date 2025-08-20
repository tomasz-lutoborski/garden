import { describe, it, expect, beforeEach } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useWorkflow } from './store'

describe('workflow store', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
  })

  it('seeds initial state', () => {
    const wf = useWorkflow()
    wf.seed()

    expect(wf.bank.length).toBe(3)
    expect(wf.dailySlots.every((s) => s === null)).toBe(true)
    expect(wf.activeTaskId).toBeNull()
  })

  it('adds a task a bank', () => {
    const wf = useWorkflow()
    wf.seed()
    wf.addTask('new task')

    expect(wf.bank.length).toBe(4)
    expect(wf.bank.map((t) => t.title)).toContain('new task')
  })

  it('moves a task from a bank to a daily slot', () => {
    const wf = useWorkflow()
    wf.seed()
    const taskId = wf.bank[0].id

    wf.moveToDaily(taskId, 0)

    expect(wf.daily[0]).toBe(taskId)
    expect(wf.bank.find((t) => t.id === taskId)).toBeUndefined()
  })

  it('clears a daily slot', () => {
    const wf = useWorkflow()
    wf.seed()
    const taskId = wf.bank[0].id

    wf.moveToDaily(taskId, 0)
    wf.clearDailySlot(0)

    expect(wf.daily[0]).toBeNull()
    expect(wf.bank.find((t) => t.id === taskId)).toBeDefined()
  })

  it('activates a task from a daily slot', () => {
    const wf = useWorkflow()
    wf.seed()
    const taskId = wf.bank[0].id

    wf.moveToDaily(taskId, 0)
    wf.activateFromDaily(0)

    expect(wf.activeTaskId).toBe(taskId)
    expect(wf.activeTask?.id).toBe(taskId)
  })

  it('resetDay returns all slot tasks to bank and resets session', () => {
    const wf = useWorkflow()
    wf.seed()
    const ids = wf.bank.map((t) => t.id)

    wf.moveToDaily(ids[0], 0)
    wf.moveToDaily(ids[1], 1)
    wf.activateFromDaily(0)

    wf.resetDay()

    expect(wf.daily.every((x) => x === null)).toBe(true)
    expect(wf.activeTaskId).toBeNull()
    expect(wf.bank.length).toBeGreaterThanOrEqual(2)
    expect(wf.session.status).toBe('idle')
    expect(wf.session.focusTotalMs).toBe(0)
    expect(wf.session.breakTotalMs).toBe(0)
  })
})
