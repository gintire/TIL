type Report interface { // Report() 메서드를 포함한 Report 인터페이스
	Report() string
}
type FinanceReport struct { // 경제 보고서를 담당하는 FinanceReport
	report string
}

func (r *FinanceReport) Report() string { // Report 인터페이스 구현
	return r.report
}

type ReportSender struct {
	...
}

func (s *ReportSender) SendReport(report Report) {
	// Report 인터페이스 객체를 인수로 받음
	...
}