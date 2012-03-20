
namespace erl ecc
namespace java ecc



exception EccException {
  1: string why
}
enum ContentType{
   Integer = 1,
   String = 2,
   Binaray = 3,
   Float = 4,
   Boolean = 5
}

struct Content {
  1:ContentType type,
  2:binary value
}
struct GenericRequester {
  
}
struct GenericResultWaiter {
  
}

service RemoteDispatcher {
   map<string,Content> runSync1(1:string serviceName, 2:map<string, Content> context) throws (1:EccException ec),
   map<string,Content> runSync2(1:string serviceName, 2:map<string, Content> context, 3:i32 transactionTimeout, 4:bool requireNewTransaction) throws (1:EccException ec),
   void runSyncIgnore1(1:string serviceName, 2:map<string, Content> context) throws (1:EccException ec),
   void runSyncIgnore2(1:string serviceName, 2:map<string, Content> context, 3:i32 transactionTimeout, 4:bool requireNewTransaction) throws (1:EccException ec),
   void runAsync1(1:string serviceName, 2:map<string, Content> context,3:GenericRequester requester, 4:bool persist, 5:i32 transactionTimeout, 6:bool requireNewTransaction) throws (1:EccException ec),
   void runAsync2(1:string serviceName, 2:map<string, Content> context,3:GenericRequester requester, 4:bool persist) throws (1:EccException ec),
   void runAsync3(1:string serviceName, 2:map<string, Content> context,3:GenericRequester requester) throws (1:EccException ec),
   void runAsync4(1:string serviceName, 2:map<string, Content> context,3:bool persist) throws (1:EccException ec),
   void runAsync5(1:string serviceName, 2:map<string, Content> context) throws (1:EccException ec),
   GenericResultWaiter runAsyncWait1(1:string serviceName, 2:map<string, Content> context,3:bool persist) throws (1:EccException ec),
   GenericResultWaiter runAsyncWait2(1:string serviceName, 2:map<string, Content> context) throws (1:EccException ec),
   void schedule1(1:string serviceName, 2:map<string, Content> context, 3:i64 startTime, 4:i32 frequency, 5:i32 interval, 6:i32 count, 7:i64 endTime) throws (1:EccException ec),
   void schedule2(1:string serviceName, 2:map<string, Content> context, 3:i64 startTime, 4:i32 frequency, 5:i32 interval, 6:i32 count) throws (1:EccException ec),
   void schedule3(1:string serviceName, 2:map<string, Content> context, 3:i64 startTime, 4:i32 frequency, 5:i32 interval, 6:i64 endTime) throws (1:EccException ec),
   void schedule4(1:string serviceName, 2:map<string, Content> context, 3:i64 startTime) throws (1:EccException ec)
}

