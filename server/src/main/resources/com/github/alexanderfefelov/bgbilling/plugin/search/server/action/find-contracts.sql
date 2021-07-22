select
  y.source,
  y.contractId,
  y.contractNo,
  y.contractStartDate,
  y.contractExpirationDate,
  y.contractComment,
  y.contractPostpaidMode,
  y.contractLimit,
  y.contractBalance,
  y.contractBalanceChangedAt,
  group_concat(concat(tp.title, ', ', ct.date1) separator '\n') as 'contractPricingPlans'
from
  (
    select
      group_concat(x.source separator '\n') as 'source',
      x.contractId,
      c.title as 'contractNo',
      coalesce(c.date1, '2042-06-04') as 'contractStartDate',
      coalesce(c.date2, '2042-06-04') as 'contractExpirationDate',
      c.comment as 'contractComment',
      c.mode = 0 as 'contractPostpaidMode',
      c.closesumma as 'contractLimit',
      (
        select
          coalesce(cb.summa1 + cb.summa2 - cb.summa3 - cb.summa4, 0)
        from
          contract_balance cb
          join (
            select
              cid,
              max(date(concat(lcb.yy, '-', lcb.mm, '-01')))
            from
              contract_balance lcb
            group by
              lcb.cid
            ) last_contract_balance on last_contract_balance.cid = cb.cid
        where
          cb.cid = c.id
      ) as 'contractBalance',
      (
        select
          date_format(max(date(concat(cbca.yy, '-', cbca.mm, '-01'))), '%Y-%m')
        from
          contract_balance cbca
        where
          cbca.cid = c.id
      ) as contractBalanceChangedAt
    from
      (
        select
          'Контракт' as 'source',
          c.id as 'contractId'
        from
          contract c
        where
          convert(c.id, char) = ?
          or c.title regexp ?
          or c.comment regexp ?

        union

        select
          cpp.title as 'source',
          cpt1.cid as 'contractId'
        from
          contract_parameter_type_1 cpt1
          left join contract_parameters_pref cpp on cpp.id = cpt1.pid
        where
          cpt1.val regexp ?

        union

        select
          cpp.title as 'source',
          cpt3.cid as 'contractId'
        from
          contract_parameter_type_3 cpt3
          left join contract_parameters_pref cpp on cpp.id = cpt3.pid
        where
          cpt3.email regexp ?

        union

        select
          cpp.title as 'source',
          cptp.cid as 'contractId'
        from
          contract_parameter_type_phone cptp
          left join contract_parameters_pref cpp on cpp.id = cptp.pid
        where
          cptp.value regexp ?

        union

        select
          'Заметки' as 'source',
          cc.cid as 'contractId'
        from
          contract_comment cc
        where
          cc.comment regexp ?
        group by
          cc.cid
      ) x
      join contract c on c.id = x.contractId
    group by
      x.contractId
  ) y
  left join contract_tariff ct on ct.cid = y.contractId
  left join tariff_plan tp on tp.id = ct.tpid
group by
  y.contractId
