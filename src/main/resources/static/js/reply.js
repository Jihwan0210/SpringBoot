async function get1(bno) { // 함수 내부에서 await 사용 가능
    const result = await axios.get(`/replies/list/${bno}`)
    // 서버 응답이 올 때까지 기다림.비동기호출
    console.log(result)
}