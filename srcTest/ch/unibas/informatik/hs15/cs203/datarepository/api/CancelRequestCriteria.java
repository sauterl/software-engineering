package ch.unibas.informatik.hs15.cs203.datarepository.api;

interface CancelRequestCriteria
{
  boolean cancelRequested(int numberOfCancelRequestChecks, long numberOfBytes, long totalNumberOfBytes);
}