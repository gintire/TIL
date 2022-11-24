type ReportSender interface {
	Send(r *Report)
}

type EmailSender struct {
}

func (e *EmailSender) Send(r *Report) {
	// 이메일 전송
}

type FaxSender struct {
}

func (f *FaxSender) Send(r *Report) {
	// 팩스 전송
}