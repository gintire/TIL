package main

import (
	"fmt"
	"math"
)

type ErrNegativeSqrt float64

func (e ErrNegativeSqrt) Error() string {
	return fmt.Sprintf("cannot Sqrt negative number: %v", float64(e))
}

func Sqrt(x float64) (float64, error) {
	if x < 0 {
		return 0, ErrNegativeSqrt(x)
	}
	// Newton-Raphson Method
	z := float64(1)
	tolerance := 1e-6

	for {
		oldz := z
		z -= (z*z - x) / (2 * z)
		if math.Abs(z-oldz) < tolerance {
			break
		}
	}
	return z, nil
}

func main() {
	fmt.Println(Sqrt(2))
	if v, err := Sqrt(-2); err != nil {
		fmt.Println(err)
	} else {
		fmt.Println(v)
	}
}
