// 85번 줄 수정

	@Override
	public int detailTripUpdate(Properties prop) {
		return plMapper.detailTripUpdate(prop);
	}

// 90번 줄 추가
	@Override
	public int updateReserve(Properties prop) {
		return plMapper.updateReserve(prop);
	}

	
}
