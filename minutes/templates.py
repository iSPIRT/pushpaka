from dateutil.rrule import *
from dateutil.parser import *
dst = isoparse("2022-02-01T20:30+05:30")
daterule = rrule(WEEKLY,dtstart=dst,count=40,byweekday=[TU,FR,SU])

dates = list(daterule)
ms = []
mom_t = """# Session {meeting.index}: {date}

Go to: [All minutes](../../) [Previous]({prev}) [Next]({next})

## Agenda

1. 

## Attendees

...

## Minutes

1. ...
1. ...
"""

class Meeting:
    def __init__(self, index, date, prev, next):
        self.index = index
        self.date = date
        self.prev = prev
        self.next = next

    def __repr__(self):
        return "Meeting({},{},{},{})".format(self.index, self.date, self.prev, self.next)

def format_date(d):
    if d:
        return d.strftime("%A, %d %B %Y")
    return ""

def format_date_md_link(d):
    if d:
        return "../../{d}.md".format(d=d.strftime("%Y/%m/%d"))
    return ""


i = 51
pd = None
for di in range(len(dates)):
    d = dates[di]
    print(d)
    if di <= (len(dates) -2):
        nd = dates[di + 1]
    else:
        nd = None

    m = Meeting(i, d, pd, nd)
    # print(m)
    ms.append(m)
    pd = d
    i += 1
    mom_c = mom_t.format(meeting=m, date=format_date(m.date), prev=format_date_md_link(m.prev), next=format_date_md_link(m.next))

    if i > 52:
        fn = "docs/minutes/{}.md".format(d.strftime("%Y/%m/%d"))
        print("saving to file |%s|" % fn)
        with open(fn, "w") as f:
            f.write(mom_c)

from itertools import groupby

for m, g in groupby(dates, lambda x: x.month):
    print("Month %d" % m)
    print(", ".join((map(lambda y: y.strftime("[%d](%Y/%m/%d.md)"), list(g)))))
