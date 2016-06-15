(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('MissionStatusDialogController', MissionStatusDialogController);

    MissionStatusDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MissionStatus', 'Mission'];

    function MissionStatusDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MissionStatus, Mission) {
        var vm = this;

        vm.missionStatus = entity;
        vm.clear = clear;
        vm.save = save;
        vm.missions = Mission.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.missionStatus.id !== null) {
                MissionStatus.update(vm.missionStatus, onSaveSuccess, onSaveError);
            } else {
                MissionStatus.save(vm.missionStatus, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:missionStatusUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
