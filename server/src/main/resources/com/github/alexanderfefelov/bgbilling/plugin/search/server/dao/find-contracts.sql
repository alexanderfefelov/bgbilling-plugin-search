select
  group_concat(x.source separator '\n') as 'source',
  x.contractId,
  x.contractNo,
  x.contractStartDate,
  x.contractExpirationDate,
  x.contractComment
from
  (
    select
      'Контракт' as 'source',
      c.id as 'contractId',
      c.title as 'contractNo',
      coalesce(c.date1, '2042-04-01') as 'contractStartDate',
      coalesce(c.date2, '2042-04-01') as 'contractExpirationDate',
      c.comment as 'contractComment'
    from
      contract c
    where
      convert(c.id, char) = ?
      or c.title regexp ?
      or c.comment regexp ?
    union
    select
      cpp.title as 'source',
      c.id as 'contractId',
      c.title as 'contractNo',
      coalesce(c.date1, '2042-04-01') as 'contractStartDate',
      coalesce(c.date2, '2042-04-01') as 'contractExpirationDate',
      c.comment as 'contractComment'
    from
      contract c
      join contract_parameter_type_1 cpt1 on cpt1.cid = c.id
      left join contract_parameters_pref cpp on cpp.id = cpt1.pid
    where
      cpt1.val regexp ?
    union
    select
      cpp.title as 'source',
      c.id as 'contractId',
      c.title as 'contractNo',
      coalesce(c.date1, '2042-04-01') as 'contractStartDate',
      coalesce(c.date2, '2042-04-01') as 'contractExpirationDate',
      c.comment as 'contractComment'
    from
      contract c
      join contract_parameter_type_3 cpt3 on cpt3.cid = c.id
      left join contract_parameters_pref cpp on cpp.id = cpt3.pid
    where
      cpt3.email regexp ?
    union
    select
      cpp.title as 'source',
      c.id as 'contractId',
      c.title as 'contractNo',
      coalesce(c.date1, '2042-04-01') as 'contractStartDate',
      coalesce(c.date2, '2042-04-01') as 'contractExpirationDate',
      c.comment as 'contractComment'
    from
      contract c
      join contract_parameter_type_phone cptp on cptp.cid = c.id
      left join contract_parameters_pref cpp on cpp.id = cptp.pid
    where
      cptp.value regexp ?
    union
    select
      'Заметки' as 'source',
      c.id as 'contractId',
      c.title as 'contractNo',
      coalesce(c.date1, '2042-04-01') as 'contractStartDate',
      coalesce(c.date2, '2042-04-01') as 'contractExpirationDate',
      c.comment as 'contractComment'
    from
      contract c
      join contract_comment сс on сс.cid = c.id
    where
      сс.comment regexp ?
    group by
      c.id
  ) x
group by
  x.contractId
